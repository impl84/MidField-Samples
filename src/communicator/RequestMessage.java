
package communicator;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: RequestMessage
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
    // ���������̃R���X�g���N�^ (Gson�V���A���C�Y�Ή�)
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
