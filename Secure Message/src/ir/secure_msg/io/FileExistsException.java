/*
 	Copyright 2012 Pedram Veisi 

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

package ir.secure_msg.io;

public class FileExistsException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String exception;

	public FileExistsException() {
		super();
		exception = "Unknown";
	}

	public FileExistsException(String exp) {
		super(exp);
		this.exception = exp;
	}

	public String getException() {
		return this.exception;
	}
}