const getCartesBancaires = 'SELECT * FROM cartesbancaires;';
const getCartesBancairesById = 'SELECT * FROM cartesbancaires WHERE iduser = $1';
const getCartesBancairesDetails = 'SELECT codecarte, codesecret FROM cartesbancaires WHERE iduser = $1';

const addCarteBancaire = 'INSERT INTO cartesbancaires (codeCarte, codesecret) VALUES ($1, $2);';

const deleteCarteBancaire = 'DELETE FROM cartesbancaires WHERE iduser = $1;';

const updateCarteBancaire = 'UPDATE cartesbancaires SET (codeCarte, codesecret) ($1, $2) WHERE iduser = $3;';

module.exports= {
    getCartesBancaires,
    getCartesBancairesById,
    getCartesBancairesDetails,
    addCarteBancaire,
    deleteCarteBancaire,
    updateCarteBancaire,
}