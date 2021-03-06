/*  Copyright (C) 2003-2011 JabRef contributors.
    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 2 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License along
    with this program; if not, write to the Free Software Foundation, Inc.,
    51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*/
package net.sf.jabref.exporter.layout.format;

import net.sf.jabref.Globals;
import net.sf.jabref.exporter.layout.LayoutFormatter;

public class RemoveLatexCommands implements LayoutFormatter {


    @Override
    public String format(String field) {

        StringBuilder sb = new StringBuilder("");
        StringBuffer currentCommand = null;
        char c;
        boolean escaped = false;
        boolean incommand = false;
        int i;
        for (i = 0; i < field.length(); i++) {
            c = field.charAt(i);
            if (escaped && c == '\\') {
                sb.append('\\');
                escaped = false;
            }
            else if (c == '\\') {
                escaped = true;
                incommand = true;
                currentCommand = new StringBuffer();
            }
            else if (!incommand && (c == '{' || c == '}')) {
                // Swallow the brace.
            }

            else if (Character.isLetter(c) ||
                    Globals.SPECIAL_COMMAND_CHARS.contains("" + c)) {
                escaped = false;
                if (!incommand) {
                    sb.append(c);
                } else {
                    currentCommand.append(c);
                    if (currentCommand.length() == 1
                            && Globals.SPECIAL_COMMAND_CHARS.contains(currentCommand.toString())) {
                        // This indicates that we are in a command of the type \^o or \~{n}
                        /*            if (i >= field.length()-1)
                                      break testCharCom;

                                    String command = currentCommand.toString();
                                    i++;
                                    c = field.charAt(i);
                                    //System.out.println("next: "+(char)c);
                                    String combody;
                                    if (c == '{') {
                                      IntAndString part = getPart(field, i);
                                      i += part.i;
                                      combody = part.s;
                                    }
                                    else {
                                      combody = field.substring(i,i+1);
                                      //System.out.println("... "+combody);
                                    }
                                    Object result = Globals.HTMLCHARS.get(command+combody);
                                    if (result != null)
                                      sb.append((String)result);
                        */
                        incommand = false;
                        escaped = false;

                    }

                }
            }

            else if (Character.isLetter(c)) {
                escaped = false;
                if (!incommand) {
                    sb.append(c);
                // Else we are in a command, and should not keep the letter.
                } else {
                    currentCommand.append(c);
                }
            }
            else {
                //if (!incommand || ((c!='{') && !Character.isWhitespace(c)))
                if (!incommand || !Character.isWhitespace(c) && c != '{') {
                    sb.append(c);
                } else {
                    if (c != '{') {
                        sb.append(c);
                    }
                }
                incommand = false;
                escaped = false;
            }
        }

        return sb.toString();
    }
}
