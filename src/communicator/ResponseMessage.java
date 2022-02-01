
package communicator;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: ResponseMessage
 *
 * Date Modified: 2021.10.27
 *
 */
class ResponseMessage
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    //
    private final String responder;
    private final String answer;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PUBLIC METHOD: OVERRIDES: Object
// -----------------------------------------------------------------------------
    
    // - PUBLIC METHOD ---------------------------------------------------------
    // - OVERRIDES: Object
    //
    @Override
    public String toString()
    {
        String string = String.format(
            "responder: %s, answer: %s",
            this.responder,
            this.answer
        );
        return string;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    ResponseMessage(String responder, String answer)
    {
        this.responder = responder;
        this.answer = answer;
    }
    
    // - CONSTRUCTOR -----------------------------------------------------------
    // 引数無しのコンストラクタ (Gsonシリアライズ対応)
    //
    ResponseMessage()
    {
        this.responder = null;
        this.answer = null;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    String getResponder()
    {
        return this.responder;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    String getAnswer()
    {
        return this.answer;
    }
}
