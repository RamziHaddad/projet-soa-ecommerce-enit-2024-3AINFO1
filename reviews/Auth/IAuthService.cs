namespace ReviewService.Auth
{
    public interface IAuthService
    {
        Task<string> AuthenticateAsync(string username, string password);
        Task<Guid> GetUserIdFromTokenAsync(string token);
    }
}
