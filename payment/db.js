const { Pool } = require ('pg');

const pool = new Pool ({
    user: 'postgres',
    host: 'localhost',
    database: 'db_payment',
    password: 'azerty',
    port: 5438
});

module.exports = pool