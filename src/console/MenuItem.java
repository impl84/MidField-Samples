
package console;

/*----------------------------------------------------------------------------*/
/**
 * Sample code of MidField System API: MenuItem
 *
 * Date Modified: 2022.06.08
 *
 */
class MenuItem
{
    // - PROTECTED CONSTANT VALUE ----------------------------------------------
    private static final int EMPTY_MENU = -1;
    
// =============================================================================
// INTERFACE:
// =============================================================================
    
    interface MenuAction
    {
        ConsoleMenu action();
    }
    
// =============================================================================
// INSTANCE VARIABLE:
// =============================================================================
    
    // - PRIVATE VARIABLE ------------------------------------------------------
    private int        menuNumber  = EMPTY_MENU;
    private String     description = null;
    private MenuAction menuAction  = null;
    
// =============================================================================
// INSTANCE METHOD:
// =============================================================================
    
// -----------------------------------------------------------------------------
// PACKAGE METHOD:
// -----------------------------------------------------------------------------
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    MenuItem()
    {
        //
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    MenuItem(int number, String desc, MenuAction action)
    {
        this.menuNumber = number;
        this.description = desc;
        this.menuAction = action;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    boolean isEmptyMenu()
    {
        boolean isEmpty = false;
        
        if (this.menuNumber == EMPTY_MENU) {
            isEmpty = true;
        }
        return isEmpty;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    int getMenuNumber()
    {
        return this.menuNumber;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    String getDescription()
    {
        return this.description;
    }
    
    // - PACKAGE METHOD --------------------------------------------------------
    //
    ConsoleMenu action()
    {
        ConsoleMenu nextMenu = null;
        
        if (this.menuAction != null) {
            nextMenu = this.menuAction.action();
        }
        return nextMenu;
    }
}
