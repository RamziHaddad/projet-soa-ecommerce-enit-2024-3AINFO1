const { Router } = require('express');
const controller = require('./controller');

const router = Router();

router.get('/', controller.getCartesBancaires);
router.get('/:iduser', controller.getCartesBancaires);

router.post('/', controller.addCarteBancaire);

router.delete('/:iduser', controller.deleteCarteBancaire);

router.put('/:iduser', controller.updateCarteBancaire);

module.exports = router;