const getAllPayments = 'SELECT id, amount, cardNumber, cardCode FROM payments;';
const getAllPaymentsExplicit = 'SELECT * FROM payments;';
const getPaymentById = 'SELECT id, amount, cardNumber, cardCode FROM payments WHERE id = $1;';
const getPaymentByIdExplicit = 'SELECT * FROM payments WHERE id = $1;';

const addPayment = 'INSERT INTO payments (amount, cardNumber, cardCode, customerId) VALUES($1, $2, $3, $4);';

const paymentWentThrough = 'UPDATE payments SET wentThrough = true WHERE id = $1;';

module.exports = {
    getAllPayments,
    getAllPaymentsExplicit,
    getPaymentById,
    getPaymentByIdExplicit,
    addPayment,
    paymentWentThrough
}