const jwt = require('jsonwebtoken');

function generateToken(payload) {
    const secret = process.env.JWT_SECRET || "default-secret";
    const expiresIn = process.env.JWT_EXPIRES_IN || "1h";
    return jwt.sign(payload, secret, { expiresIn });
}

function verifyToken(token) {
    const secret = process.env.JWT_SECRET || "default-secret";

    try {
        return jwt.verify(token, secret);
    } catch (error) {
        console.error('JWT validation failed:', error.message);
        return null; 
    }
}

module.exports = { generateToken, verifyToken };
