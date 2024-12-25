const pool = require("../../db");
const queries = require('./queries');

const getCartesBancaires = async (req, res) => {
    try {
        const data = await pool.query(queries.getCartesBancaires)
        res.status(200).send(data.rows)
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }
}

const getCartesBancairesById = async (req, res) => {
    const id = req.params.iduser 

    try {
        const data = await pool.query(queries.getCartesBancairesById, [id])
        res.status(200).send(data.rows)
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }  
}

const addCarteBancaire = async (req, res) => {
    const { codeCarte, codeSecret } = req.body

    try {
        await pool.query(queries.addCarteBancaire, [codeCarte, codeSecret])
        res.status(200).send({"Successfully added card": codeCarte})
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    }
}

const deleteCarteBancaire = async (req, res) => {
    const id = parseInt(req.params.iduser)

    pool.query(queries.getCartesBancairesById, [id], (errer, results) => {
        const noCarteBancaireFound = !results.rows.length;
        if (noCarteBancaireFound) {
            res.send("Card does not exist in the database");
        }
    })

    try {
        const data = await pool.query(queries.deleteCarteBancaire, [id])
        res.status(200).send({"Successfully deleted card": id})
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    } 
}

const updateCarteBancaire = async (req, res) => {
    const id = parseInt(req.params.iduser)
    const { codeCarte, codeSecret } = req.body

    pool.query(queries.getCartesBancairesById, [id], (errer, results) => {
        const noCarteBancaireFound = !results.rows.length;
        if (noCarteBancaireFound) {
            res.send("Card does not exist in the database");
        }
    })
    
    try {
        const data = await pool.query(queries.updateCarteBancaire, [codeCarte, codeSecret, id])
        res.status(200).send({"Successfully updated card": id})
    } catch (err) {
        console.log(err)
        res.sendStatus(500)
    } 
}

module.exports = {
    getCartesBancaires,
    getCartesBancairesById,
    addCarteBancaire,
    deleteCarteBancaire,
    updateCarteBancaire,
};