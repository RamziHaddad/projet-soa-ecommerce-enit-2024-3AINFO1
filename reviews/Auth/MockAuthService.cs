namespace ReviewService.Auth
{
    public class MockAuthService : IAuthService
    {
        public Task<string> AuthenticateAsync(string username, string password)
        {
            // Vous pouvez utiliser des informations fictives pour l'utilisateur
            if (username == "testuser" && password == "password")
            {
                // Retourne un token fictif (un token JWT simulé)
                return Task.FromResult("fake-jwt-token");
            }

            // Si l'authentification échoue
            return Task.FromResult<string>(null);
        }

        public Task<Guid> GetUserIdFromTokenAsync(string token)
        {
            // Vérifie si le token est valide et retourne un userId fictif
            if (token == "fake-jwt-token")
            {
                return Task.FromResult(Guid.NewGuid()); // ID fictif de l'utilisateur
            }

            // Si le token est invalide
            return Task.FromResult(Guid.Empty);
        }
    }

}
