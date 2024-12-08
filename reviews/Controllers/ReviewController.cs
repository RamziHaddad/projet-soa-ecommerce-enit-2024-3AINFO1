using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using ReviewService.Auth;
using ReviewService.Data;

namespace ReviewService.Controllers
{
    [ApiController]
    [Route("api/reviews")]
    public class ReviewController : Controller
    {
        private readonly ReviewContext _context;
        private readonly IAuthService _authService;
        private readonly HttpClient _httpClient;
        private readonly string _catalogServiceUrl = "http://localhost:8082/product";

        public ReviewController(HttpClient httpClient, ReviewContext context, IAuthService authService)
        {
            _context = context;
            _authService = authService;
            _httpClient = httpClient;
        }

        [HttpPost]
        public async Task<IActionResult> AddReview([FromBody] ReviewService.Models.Review review, [FromHeader] string token)
        {
            // Vérifie le token d'authentification
            var userId = await _authService.GetUserIdFromTokenAsync(token);
            if (userId == Guid.Empty)
            {
                return Unauthorized(new { message = "Token d'authentification invalide." });
            }

            // Vérifie la validité des entrées
            if (review.ProduitId == Guid.Empty || review.UserId == Guid.Empty)
            {
                return BadRequest(new { message = "ProduitId et UserId ne peuvent pas être null ou vides." });
            }

            // Vérifier si le produit existe dans le catalogue
            var response = await _httpClient.GetAsync($"{_catalogServiceUrl}/id/{review.ProduitId}");
            if (!response.IsSuccessStatusCode)
            {
                return NotFound(new { message = "Produit non trouvé dans le catalogue." });
            }

            // Vérifier s'il existe déjà un avis pour ce produit et cet utilisateur
            var existingReview = await _context.Reviews
                .FirstOrDefaultAsync(r => r.ProduitId == review.ProduitId && r.UserId == userId);

            if (existingReview != null)
            {
                return Conflict(new { message = "Vous avez déjà ajouté un avis pour ce produit." });
            }

            // Ajout de l'avis
            review.UserId = userId;
            _context.Reviews.Add(review);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetReviewsByProduct), new { idProduit = review.ProduitId }, review);
        }

        [HttpGet("{idProduit}")]
        public async Task<ActionResult<IEnumerable<ReviewService.Models.Review>>> GetReviewsByProduct(Guid idProduit)
        {
            // Récupère les avis pour un produit spécifique
            var commentaires = await _context.Reviews
                .Where(r => r.ProduitId == idProduit)
                .Select(r => r.Commentaire)
                .ToListAsync();

            if (!commentaires.Any())
            {
                return NotFound(new { message = "Aucun avis trouvé pour ce produit." });
            }

            return Ok(commentaires);
        }
    }
}
