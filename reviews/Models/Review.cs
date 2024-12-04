using System.ComponentModel.DataAnnotations;

namespace ReviewService.Models
{
    public class Review
    {
        [Key]
        public Guid Id { get; set; } = Guid.NewGuid();
        public Guid ProduitId { get; set; }
        public Guid UserId { get; set; }
        [Required]
        [StringLength(500, ErrorMessage = "Le commentaire ne peut pas dépasser 500 caractères.")]
        public required string Commentaire { get; set; }
        public DateTime dateCreation { get; set; } = DateTime.UtcNow;
    }
}
