const { Router } = require ('express');
const controller = require ('./controller');
const orderController = require ('../orders/controller');

const router = Router();

router.get('/', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.getAllPayments);
router.get('/explicit', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.getAllPaymentsExplicit);
router.get('/:id', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.getPaymentById);
router.get('/explicit/:id', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.getPaymentByIdExplicit);
router.get('/result/:id', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.getPaymentResult);

router.post('/process/', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), orderController.returnPaymentResult);
router.post('/', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.addPayment);

router.put('/:id', keycloak.protect('ECOMMERCE_USER', 'ECOMMERCE_ADMIN'), controller.paymentWentThrough);

module.exports = router;