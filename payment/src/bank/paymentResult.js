const axios = require ('axios');
const queries = require ('../payments/queries');
const pool = require ('../db');

const fetchPaymentResult = async (id) => {
    try {
        const result = await pool.query(queries.getPaymentById, [id]);

        if (result.rows.length === 0) {
            console.log('No payment found');
            return;
        }

        const { transaction_id, amount, card_number, card_code } = result.rows[0];

        const payload = {
            transactionId: transaction_id, 
            amount: amount, 
            cardNumber: card_number,
            cardCode: card_code, 
          };

        const response = await axios.post(`http://localhost:8099/payments/`, payload);

        switch (response.status) {
            case 200:
                pool.query(queries.paymentWentThrough, [id]);
                console.log('Payment went through', response.data);
                break;
            case 409:
                console.log('Payment is a duplicate', response.data);
                break;
            case 417:
                console.log('Invalid payment details', response.data);
                break;
            default:
                console.log('Unknown response status:', response.status);
                break;
        } 
    } catch (error) {
        throw error;
    }
}

module.exports = {
    fetchPaymentResult
}