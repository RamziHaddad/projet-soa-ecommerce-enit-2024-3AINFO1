const express = require ('express');
const cartesbancairesRoutes = require('./src/cartesbancaires/routes');
const paymentsRoutes = require('./src/payments/routes');
const pool = require('./db');

const port = 8085;

const app = express();

app.use(express.json());
app.use('/payment/cartesbancaires', cartesbancairesRoutes);
app.use('/payments', paymentsRoutes);




app.listen(port, () => {
    console.log(`Payment service listening on port ${port}`);
})