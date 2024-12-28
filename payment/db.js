// db.js
const { Pool } = require('pg');

const pool = new Pool({
  connectionString: process.env.DATABASE_URL, // This uses the DATABASE_URL from .env
});


module.exports = pool;
