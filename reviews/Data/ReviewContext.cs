using Microsoft.EntityFrameworkCore;

namespace ReviewService.Data
{ 
    public class ReviewContext : DbContext
    {
        public ReviewContext(DbContextOptions<ReviewContext> options) : base(options) { }
        public DbSet<ReviewService.Models.Review> Reviews { get; set; }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.Entity<ReviewService.Models.Review>(entity =>
            {
                entity.ToTable("reviews");
                entity.HasKey(r => r.Id);
                entity.Property(r => r.Id).HasDefaultValueSql("gen_random_uuid()"); // Génération automatique UUID
                entity.Property(r => r.ProduitId).IsRequired();
                entity.Property(r => r.UserId).IsRequired();
            });
        }
    }
}
