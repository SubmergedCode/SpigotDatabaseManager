/**
 * Copyright (c) 2014 SubmergedCode
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package uk.submergedcode.spigotdatabasemanager;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main Plugin class, loaded by the plugin loader at runtime.
 * 
 * @author Sam Oates <sam@samoatesgames.com>
 */
public class SpigotDatabaseManagerPlugin extends JavaPlugin {
        
    /**
     * Called when the plugin is loaded.
     */
    @Override
    public void onLoad() {
        SpigotDatabaseManager.getInstance().setPlugin(this);
    }
    
    /**
     * Called when the plugin is disabled.
     */
    @Override
    public void onDisable() {
        SpigotDatabaseManager.getInstance().onDisable();
    }
    
}
