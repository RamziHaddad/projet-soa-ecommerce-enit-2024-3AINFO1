const { Router } = require('express');
const controller = require('./controller');
const keycloak = require('./src/Config/keycloakConfig');

const router = Router();

router.get('/', keycloak.protect('ECOMMERCE_ADMIN'), controller.getCartesBancaires);
router.get('/:iduser', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.getCartesBancairesById);

router.post('/', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.addCarteBancaire);

router.delete('/:iduser', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.deleteCarteBancaire);

router.put('/:iduser', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.updateCarteBancaire);

module.exports = router;