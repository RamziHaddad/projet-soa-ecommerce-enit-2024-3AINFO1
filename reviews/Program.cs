using Microsoft.EntityFrameworkCore;
using ReviewService.Auth;
using ReviewService.Data;


var builder = WebApplication.CreateBuilder(args);

// Configuration de la cha�ne de connexion pour PostgreSQL
builder.Services.AddDbContext<ReviewContext>(options =>
    options.UseNpgsql(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddScoped<IAuthService, MockAuthService>();

// Ajout des services n�cessaires
builder.Services.AddControllers();
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();
builder.Services.AddHttpClient();

var app = builder.Build();

// Appliquer les migrations au d�marrage de l'application
using (var scope = app.Services.CreateScope())
{
    var dbContext = scope.ServiceProvider.GetRequiredService<ReviewContext>();
    dbContext.Database.Migrate();
}

// Configuration du pipeline de requ�tes HTTP
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.Urls.Add("http://localhost:8088");

app.UseHttpsRedirection();

app.UseAuthorization();

app.MapControllers();

app.Run();
