const { axios } = require ('axios');
const queriesCards = require ('../cartesbancaires/queries');
const queriesPayments = require ('../payments/queries');
const pool = require ('../../db');
const resultPayment = require ('../bank/paymentResult');


const returnPaymentResult = async (id) => {
    try {
        const response = await axios.get(`http://localhost:8084/orders/${id}`);
        const orderDetails = response.data;
    
        const orderId = orderDetails.orderId;
        const customerId = orderDetails.customerId;
        const amount = orderDetails.totalAmount;

        await pool.query(queriesPayments.addPaymentFromOrder, [amount, orderId, customerId]);

        const result = await pool.query(queriesPayments.getPaymentId, [orderId]);
        if (result.rows.length > 0) {
            const paymentId = result.rows[0].id;
            await resultPayment.fetchPaymentResult(paymentId);
        }


    } catch (error) {
        console.error(`Error fetching details for order with ID ${orderId}:`, error.message);
    }

    /* const { v1, v2 } = await checkClientExists(customerId); */

   
}

const checkClientExists = async (id) => {
    query = queriesCards.getCartesBancairesDetails;

    try {
        const result = await pool.query(query, [id]);
        if (result.rows.length > 0) {
            const { field1, field2 } = result.rows[0];
            return { field1, field2 };
        } else {
            return null;
        } 
    } catch (error) {
        throw error;
    }
}

module.exports = {
    returnPaymentResult,
}