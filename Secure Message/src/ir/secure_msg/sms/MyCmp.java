/*
	Copyright 2012 Mohammad Moein

    This file is part of Secure Message.

    Secure Message is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Secure Message is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Secure Message.  If not, see <http://www.gnu.org/licenses/>.

 */

package ir.secure_msg.sms;

import ir.secure_msg.database.Message;

import java.util.Comparator;




public class MyCmp implements Comparator<Message> {

	@Override
	public int compare(Message lhs, Message rhs) {
		// TODO Auto-generated method stub
		 return (lhs.getId()>rhs.getId() ? 1 : (lhs.getId()==rhs.getId() ? 0 : -1));
	}
	

}
