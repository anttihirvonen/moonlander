/**
 * ##library.name##
 * ##library.sentence##
 * ##library.url##
 *
 * Copyright ##copyright## ##author##
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General
 * Public License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA  02111-1307  USA
 * 
 * @author      ##author##
 * @modified    ##date##
 * @version     ##library.prettyVersion## (##library.version##)
 */

package rocketpower.library;

// import processing.core.*;

/**
 * PRocket
 * 
 * @example Integration 
 * 
 * (the tag @example followed by the name of an example included in folder 'examples' will
 * automatically include the example in the javadoc.)
 *
 */

public class Rocketpower {
    public final static String VERSION = "##library.prettyVersion##";

    /**
     * a Constructor, usually called in the setup() method in your sketch to
     * initialize and start the library.
     * 
     * @param theParent
     */
    public Rocketpower() {
        welcome();
    }


    private void welcome() {
        System.out.println("##library.name## ##library.prettyVersion## by ##author##");
    }

    public String sayHello() {
        return "hello library.";
    }
    /**
     * return the version of the library.
     * 
     * @return String
     */
    public static String version() {
        return VERSION;
    }
}
