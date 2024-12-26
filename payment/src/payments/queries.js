const getAllPayments = 'SELECT id, amount, card_Number, card_Code FROM payments;';
const getAllPaymentsExplicit = 'SELECT * FROM payments;';
const getPaymentById = 'SELECT id, amount, card_Number, card_Code FROM payments WHERE id = $1;';
const getPaymentByIdExplicit = 'SELECT * FROM payments WHERE id = $1;';
const getPaymentResult = 'SELECT id, wentThrough FROM payments WHERE id = $1;';
const getPaymentId = 'SELECT id FROM payments WHERE id = $1;'

/* const addPayment = 'INSERT INTO payments (amount, card_Number, card_Code, customerId) VALUES ($1, $2, $3, $4);'; */
const addPayment = 'INSERT INTO payments (amount, id , customer_Id) VALUES ($1, $2, $3);'

const paymentWentThrough = 'UPDATE payments SET wentThrough = true WHERE id = $1;';

module.exports = { 
    getAllPayments,
    getAllPaymentsExplicit,
    getPaymentById,
    getPaymentByIdExplicit,
    getPaymentResult,
    getPaymentId,
    addPayment,
    paymentWentThrough
}