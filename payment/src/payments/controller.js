const pool = require ('../../db');
const queries = require('./queries');

const getAllPayments = async (req, res) => {
    try {
        const data = await pool.query(queries.getAllPayments)
        res.status(200).send(data.rows)
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }
}

const getAllPaymentsExplicit = async (req, res) => {
    try {
        const data = await pool.query(queries.getAllPaymentsExplicit)
        res.status(200).send(data.rows)
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }
}

const getPaymentById = async (req, res) => {
    const id = req.params.id

    try {
        const data = await pool.query(queries.getPaymentById, [id])
        res.status(200).send(data.rows)
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }  
}

const getPaymentByIdExplicit = async (req, res) => {
    const id = req.params.id

    try {
        const data = await pool.query(queries.getPaymentByIdExplicit, [id])
        res.status(200).send(data.rows)
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
        //throw err;
    }  
}

getPaymentResult = async (req, res) => {
    const id = req.params.id

    try {
        const data = await pool.query(queries.getPaymentResult, [id])
        res.status(200).send(data.rows)
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }
}

const addPayment = async (req, res) => {
    const { amount, cardNumber, cardCode, customerId } = req.body

    try {
        await pool.query(queries.addPayment, [amount, cardNumber, cardCode, customerId])
        res.status(200).send("Successfully added payment")
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }
}

const paymentWentThrough = async (req, res) => {
    const id = parseInt(req.params.iduser)
    const { codeCarte, codeSecret } = req.body

    pool.query(queries.getPaymentById, [id], (errer, results) => {
        const noPaymentFound = !results.rows.length;
        if (noPaymentFound) {
            res.send("payment does not exist in the database");
        }
    })
    
    try {
        const data = await pool.query(queries.paymentWentThrough, [id])
        res.status(200).send({"Successfully updated payment state": id})
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    } 
}

module.exports = {
    getAllPayments,
    getAllPaymentsExplicit,
    getPaymentById,
    getPaymentByIdExplicit, 
    getPaymentResult,
    addPayment,
    paymentWentThrough
}