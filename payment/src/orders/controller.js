const pool = require ('../../db');
const bank = require ('../bank/paymentResult');
const payments = require ('../payments/queries');

const returnPaymentResult = async (req, res) => {
    const { amount, id, customer_id } = req.body

    try {
        await pool.query(queries.addPayment, [amount, id, customer_id])

        await bank.fetchPaymentResult(id);

        const resultPayment = await pool.query(payments.getPaymentResult, [id])
        res.status(200).send(resultPayment.rows[0]);
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }
}


module.exports = {
    returnPaymentResult,
} 