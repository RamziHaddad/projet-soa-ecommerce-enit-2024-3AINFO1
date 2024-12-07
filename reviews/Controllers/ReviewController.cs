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

        public ReviewController(ReviewContext context, IAuthService authService)
        {
            _context = context;
            _authService = authService;
        }

        [HttpPost]
        public async Task<IActionResult> AddReview([FromBody] ReviewService.Models.Review review, [FromHeader] string token)
        {
            // Vérifie le token d'authentification
            Guid userId = await _authService.GetUserIdFromTokenAsync(token);
            if (userId == Guid.Empty)
            {
                return Unauthorized("Token d'authentification invalide.");
            }

            // Si l'utilisateur est authentifié, on ajoute l'avis
            review.UserId = userId;

            if (!ModelState.IsValid)
            {
                return BadRequest(ModelState);
            }

            if (review.ProduitId == null || review.UserId == null)
            {
                return BadRequest(new { message = "ProduitId et UserId ne peuvent pas être null." });
            }

            var existingReview = await _context.Reviews
                .FirstOrDefaultAsync(r => r.ProduitId == review.ProduitId && r.UserId == review.UserId);

            if (existingReview != null)
            {
                return Conflict(new { message = "Vous avez déjà ajouté un avis pour ce produit." });
            }
            _context.Reviews.Add(review);
            await _context.SaveChangesAsync();

            return CreatedAtAction(nameof(GetReviewsByProduct), new { idProduit = review.ProduitId }, review);
        }


        [HttpGet("{idProduit}")]
        public async Task<ActionResult<IEnumerable<ReviewService.Models.Review>>> GetReviewsByProduct(Guid idProduit)
        {
            var reviews = await _context.Reviews
                .Where(r => r.ProduitId == idProduit)
                .ToListAsync();

            if (!reviews.Any())
            {
                return NotFound("Aucun avis trouvé pour ce produit.");
            }

            return Ok(reviews);
        }
    }
}
