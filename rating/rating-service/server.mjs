import 'dotenv/config';
import express from 'express';
import bodyParser from 'body-parser';
import cors from 'cors';
import pkg from 'pg';
const { Pool } = pkg;
import { generateToken, verifyToken } from './utils/jwt.js';
import { registerUser, loginUser } from './controllers/authentication.mjs';

const app = express();
app.use(bodyParser.json());
app.use(cors());

const pool = new Pool({
  host: process.env.DB_HOST,
  port: process.env.DB_PORT,
  user: process.env.DB_USER,
  password: process.env.DB_PASSWORD,
  database: process.env.DB_NAME,
});

// Middleware to validate JWT token
const authenticateToken = (req, res, next) => {
  const authHeader = req.headers['authorization'];
  if (!authHeader) {
    console.log('Authorization header missing');
    return res.sendStatus(401);
  }

  const token = authHeader.split(' ')[1];
  const user = verifyToken(token);

  if (!user) {
    console.log('JWT verification failed');
    return res.sendStatus(403);
  }

  req.user = user;
  next();
};

//GENERAL PURPOSE PERSPECTIVE

// Get ratings for a specific product
app.get('/products/:product_id/ratings', async (req, res) => {
  const product_id = req.params.product_id;
  if (!product_id) {
    return res.status(400).json({ error: 'Product ID is required' });
  }
  try {
    const result = await pool.query(
      'SELECT rating FROM ratings WHERE product_id = $1',
      [product_id]
    );

    res.json(result.rows[0]); 
  } catch (err) {
    res.status(500).json({ error: err.message }); 
  }
});


// Get average rating for a specific product
app.get('/products/:product_id/ratings/avg', async (req, res) => {
  const product_id = req.params.product_id;
  if (!product_id) {
    return res.status(400).json({ error: 'Product ID is required' });
  }
  try {
    const result = await pool.query(
      'SELECT AVG(rating) as average_rating FROM ratings WHERE product_id = $1',
      [product_id]
    );

    res.json(result.rows[0]); 
  } catch (err) {
    res.status(500).json({ error: err.message }); 
  }
});

// Delete a rating for a product
app.delete('/products/rating', async (req, res) => {
  const { product_id } = req.body;

  if (!product_id) {
    return res.status(400).json({ error: 'Product ID is required' });
  }

  try {
    const existingRating = await pool.query(
      'SELECT * FROM ratings WHERE product_id = $1',
      [product_id]
    );

    if (existingRating.rows.length === 0) {
      return res.status(404).json({ error: 'You have not rated this product yet' });
    }

    const deletedRating = await pool.query(
      'DELETE FROM ratings WHERE product_id = $1 RETURNING *',
      [product_id]
    );

    res.status(200).json(deletedRating.rows[0]);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});


//USER PERSPECTIVE

//Sign up
app.post('/register', registerUser);

//Sign in
app.post('/login', loginUser);


// Get ratings for authenticated user
app.get('/user/ratings', authenticateToken, async (req, res) => {
  const user_id = req.user.id;

  try {
    const result = await pool.query(
      'SELECT * FROM ratings WHERE user_id = $1',
      [user_id]
    );
    res.json(result.rows);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});


// Add a new rating for a product (if not already rated)
app.post('/products/rating', authenticateToken, async (req, res) => {
  const { product_id, rating } = req.body;
  const user_id = req.user.id;

  if (!product_id || !rating) {
    return res.status(400).json({ error: 'Product ID and rating are required' });
  }

  try {
    const existingRating = await pool.query(
      'SELECT * FROM ratings WHERE product_id = $1 AND user_id = $2',
      [product_id, user_id]
    );

    if (existingRating.rows.length > 0) {
      return res.status(400).json({ error: 'You have already rated this product' });
    }

    const result = await pool.query(
      'INSERT INTO ratings (product_id, user_id, rating) VALUES ($1, $2, $3) RETURNING *',
      [product_id, user_id, rating]
    );
    
    res.status(201).json(result.rows[0]);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Update an existing rating for a product
app.put('/products/rating', authenticateToken, async (req, res) => {
  const { product_id, rating } = req.body;
  const user_id = req.user.id;

  if (!product_id || !rating) {
    return res.status(400).json({ error: 'Product ID and rating are required' });
  }

  try {
    const existingRating = await pool.query(
      'SELECT * FROM ratings WHERE product_id = $1 AND user_id = $2',
      [product_id, user_id]
    );

    if (existingRating.rows.length === 0) {
      return res.status(404).json({ error: 'You have not rated this product yet' });
    }

    const updatedRating = await pool.query(
      'UPDATE ratings SET rating = $1 WHERE product_id = $2 AND user_id = $3 RETURNING *',
      [rating, product_id, user_id]
    );

    res.status(200).json(updatedRating.rows[0]);
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// Get all authenticated user ratings
app.get('/user/ratings', authenticateToken, async (req, res) => {
  const user_id = req.user.id;

  if (!user_id) {
    return res.status(400).json({ error: 'User ID is required' });
  }

  try {
    const result = await pool.query(
      'SELECT rating FROM ratings WHERE user_id = $1',
      [user_id]
    );

    const userRating = result.rows[0]?.rating || null;

    res.json({
      user_rating: userRating,
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }

});

// Get rating for a specific product (authenticated user)
app.get('/user/ratings/product', authenticateToken, async (req, res) => {
  const { product_id } = req.query;
  const user_id = req.user.id;

  if (!product_id) {
    return res.status(400).json({ error: 'Product ID is required' });
  }
  if (!user_id) {
    return res.status(400).json({ error: 'User ID is required' });
  }

  try {
    const result = await pool.query(
      'SELECT rating FROM ratings WHERE product_id = $1 AND user_id = $2',
      [product_id, user_id]
    );

    const userRating = result.rows[0]?.rating || null;

    res.json({
      product_id,
      user_rating: userRating,
    });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }

});


// Launch the server
const PORT = process.env.PORT || 8092;
app.listen(PORT, () => {
  console.log(`Rating service running on http://localhost:${PORT}`);
});

