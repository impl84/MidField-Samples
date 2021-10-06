
package desktop;

import java.io.Serializable;

//------------------------------------------------------------------------------
/**
 * Sample code of MidField System API: DesktopMessage 
 *
 * Date Modified: 2021.09.10
 *
 */

//==============================================================================
@SuppressWarnings("serial")
class DesktopMessage
	implements	Serializable
{
	//- PACKAGE CONSTANT VALUE -------------------------------------------------
	static final String MESSAGE_TYPE = "Desktop-Message";
	
	//- PACKAGE ENUM -----------------------------------------------------------
	static enum Subtype
	{
		START_CONTROL,
		CONTROL_ACCEPTED,
		CONTROL_REFUSED,
		CONTROL_MESSAGE,
		STOP_CONTROL
	}
	
	//- PACKAGE ENUM -----------------------------------------------------------
	static enum Action
	{
		KEY_PRESS,
		KEY_RELEASE,
		MOUSE_MOVE,
		MOUSE_PRESS,
		MOUSE_RELEASE,
		MOUSE_WHEEL,
		UNKNOWN
	}
	
//==============================================================================
//  INSTANCE VARIABLE:
//==============================================================================
	
	//- PRIVATE VARIABLE -------------------------------------------------------
	private final Subtype subtype;
	private final Action action;
	private final Serializable object;
	
//==============================================================================
//  INSTANCE METHOD:
//==============================================================================
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//
	DesktopMessage(Subtype subtype, Serializable object)
	{
		this.subtype = subtype;
		this.action = Action.UNKNOWN;
		this.object = object;
	}
	
	//- CONSTRUCTOR ------------------------------------------------------------
	//
	DesktopMessage(Subtype subtype, Action action, Serializable object)
	{
		this.subtype = subtype;
		this.action = action;
		this.object = object;
	}
	
//------------------------------------------------------------------------------
//  PACKAGE METHOD:
//------------------------------------------------------------------------------
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	Subtype getSubtype()
	{
		return this.subtype;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	Action getAction()
	{
		return this.action;
	}
	
	//- PACKAGE METHOD ---------------------------------------------------------
	//
	Object getObject()
	{
		return this.object;
	}
}
