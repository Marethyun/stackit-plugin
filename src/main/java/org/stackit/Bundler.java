package org.stackit;

import io.noctin.events.EntityHandler;

import java.util.LinkedList;

public final class Bundler extends StackItContainer {

    private StackItLogger LOGGER = pluginInstance.logger();

    public static final String BUNDLE_ENABLED_MESSAGE = "Bundle %s successfully enabled";

    private final boolean enabled;
    private final EntityHandler handler;
    private final StackItCommand command;

    private final LinkedList<StackItBundle> bundles = new LinkedList<>();

    public Bundler(StackIt pluginInstance) {
        super(pluginInstance);

        this.enabled = pluginInstance.getConfiguration().getBoolean(ConfigNodes.BUNDLES.getNode());
        this.handler = pluginInstance.getHandler();
        this.command = pluginInstance.getCommand();
    }


    public void registerBundle(StackItBundle bundle) throws BundlerDisabledException{
        if (this.enabled){
            bundles.add(bundle);
            LOGGER.success(String.format(BUNDLE_ENABLED_MESSAGE, bundle.getName()));
        } else {
            throw new BundlerDisabledException();
        }
    }

    public EntityHandler getEventHandler(StackItBundle bundle){
        if (bundles.contains(bundle)){
            return this.handler;
        }
        throw new BundleViolationException();
    }

    public void registerCommandOption(StackItBundle bundle, StackItCommand.Option option){
        if (bundles.contains(bundle)){
            this.command.registerOption(option);
        }
        throw new BundleViolationException();
    }
}
