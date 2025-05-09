package com.redstone.beacon.api.plugin

import java.net.URL

/**
 * Find a plugin descriptor for a plugin path.
 *
 *
 * You can find the plugin descriptor in manifest file [ManifestPluginDescriptorFinder],
 * properties file [PropertiesPluginDescriptorFinder], xml file,
 * java services (with [java.util.ServiceLoader]), etc.
 *
 * @author Decebal Suiu
 */
interface DescriptionFinder {
    /**
     * Returns `true` if this finder is applicable to the given `pluginPath`.
     * This is used to select the appropriate finder for a given plugin path.
     *
     * @param pluginPath the plugin path
     */
    fun isApplicable(pluginPath: URL): Boolean

    /**
     * Find the plugin descriptor for the given `pluginPath`.
     *
     * @param pluginPath the plugin URL
     * @return the plugin descriptor or `null` if not found
     */
    fun find(pluginURL: URL): Descriptor?

}
