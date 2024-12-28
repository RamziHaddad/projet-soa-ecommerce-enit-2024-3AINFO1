const Keycloak = require('keycloak-connect');
require('dotenv').config();


const keycloak = new Keycloak({
  store: memoryStore,
}, {
  realm: process.env.KEYCLOAK_REALM,
  authServerUrl: process.env.KEYCLOAK_AUTH_SERVER_URL,
  clientId: process.env.KEYCLOAK_CLIENT_ID,
  credentials: {
    secret: process.env.KEYCLOAK_CLIENT_SECRET,
  },
  bearerOnly: process.env.KEYCLOAK_BEARER_ONLY === 'true',
  publicClient: process.env.KEYCLOAK_PUBLIC_CLIENT === 'false',
  sslRequired: process.env.KEYCLOAK_SSL_REQUIRED,
  authorizationUri: process.env.KEYCLOAK_AUTHORIZATION_URI,
  tokenUri: process.env.KEYCLOAK_TOKEN_URI,
  userInfoUri: process.env.KEYCLOAK_USER_INFO_URI,
});

// Export the keycloak instance
module.exports = keycloak;
