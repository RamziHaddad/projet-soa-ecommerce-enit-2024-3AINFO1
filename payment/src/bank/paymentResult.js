const axios = require ('axios');
const queries = require ('../payments/queries');
const pool = require ('../../db');
const { v4: uuidv4 } = require('uuid');

const fetchPaymentResult = async (p_id) => {
    try {
        const result = await pool.query(queries.getPaymentById, [p_id]);

        if (result.rows.length === 0) {
            console.log('No payment found');
            return;
        }

        const { id, amount, card_number, card_code } = result.rows[0];

        const transactionId = uuidv4();

        const payload = {
            transactionId: id, 
            amount: amount, 
            cardNumber: parseInt(card_number),
            cardCode: parseInt(card_code), 
          };

        const response = await axios.post(`http://bank-service:8099/payments/`, payload);

        switch (response.status) {
            case 200:
                pool.query(queries.paymentWentThrough, [p_id]);
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