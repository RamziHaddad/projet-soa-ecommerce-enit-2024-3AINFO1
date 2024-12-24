const { Router } = require ('express');
const controller = require ('./controller');

const router = Router();

router.get('/', controller.getAllPayments);
router.get('/explicit', controller.getAllPaymentsExplicit);
router.get('/:id', controller.getPaymentById);
router.get('/explicit/:id', controller.getPaymentByIdExplicit);

router.post('/', controller.addPayment);

router.put('/:id', controller.paymentWentThrough);

module.exports = router;