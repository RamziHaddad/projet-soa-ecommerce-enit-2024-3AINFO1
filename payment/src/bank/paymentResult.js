const axios = require ('axios');
const queries = require ('../payments/queries');

const fetchPaymentResult = async (id) => {
    try {
        const response = await axios.get(`http://localhost:8099/payments/${id}`);

        if (response.status === 200) {
            queries.paymentWentThrough(id);
        } 
    } catch (error) {
        if (error.response && error.response.status === 404) {
          console.error('Payment not found:', id);
          // Perform the action for payment not found
          
        } else {
          throw error;
        }
    }
}

module.exports = {
    fetchPaymentResult
}