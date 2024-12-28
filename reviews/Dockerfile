# Use a smaller base image, such as the ASP.NET runtime image for .NET 8.0
FROM mcr.microsoft.com/dotnet/aspnet:8.0-slim AS base
WORKDIR /app
EXPOSE 8088

# Use the SDK image for building the app, then remove build dependencies later
FROM mcr.microsoft.com/dotnet/sdk:8.0-slim AS build
ARG BUILD_CONFIGURATION=Release
WORKDIR /src
COPY ["ReviewService.csproj", "."]
RUN dotnet restore "./ReviewService.csproj"
COPY . .
WORKDIR "/src/."
RUN dotnet build "./ReviewService.csproj" -c ${BUILD_CONFIGURATION} -o /app/build

FROM build AS publish
RUN dotnet publish "./ReviewService.csproj" -c ${BUILD_CONFIGURATION} -o /app/publish /p:UseAppHost=false

# Use the smaller base image and copy the final published files
FROM base AS final
WORKDIR /app
COPY --from=publish /app/publish .
ENTRYPOINT ["dotnet", "ReviewService.dll"]
