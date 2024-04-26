
package trial.communicator;

/**
 * 
 * Sample code for MidField API
 * 
 * Date Modified: 2021.10.27
 *
 */
class RequestMessage
{
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    //
    private final String interviewer;
    private final String question;
    
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
            "interviewer: %s, question: %s",
            this.interviewer,
            this.question
        );
        return string;
    }
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - CONSTRUCTOR -----------------------------------------------------------
    //
    RequestMessage(String interviewer, String question)
    {
        this.interviewer = interviewer;
        this.question = question;
    }
    
    // - CONSTRUCTOR -----------------------------------------------------------
    // 引数無しのコンストラクタ (Gsonシリアライズ対応)
    //
    RequestMessage()
    {
        this.interviewer = null;
        this.question = null;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    String getInterviewer()
    {
        return this.interviewer;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    String getQuestion()
    {
        return this.question;
    }
}
