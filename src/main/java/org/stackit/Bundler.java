package org.stackit;

import io.noctin.events.EntityHandler;

import java.util.LinkedList;

import static org.stackit.StackIt.PREFIX;

public final class Bundler {

    public static final String BUNDLE_ENABLED_MESSAGE = PREFIX + "Bundle %s successfully enabled";

    private final boolean enabled;
    private final StackIt mainPlugin;
    private final EntityHandler handler;
    private final StackItCommand command;

    private final LinkedList<StackItBundle> bundles = new LinkedList<>();

    public Bundler(boolean enabled, StackIt mainPlugin, EntityHandler handler, StackItCommand command) {
        this.enabled = enabled;
        this.mainPlugin = mainPlugin;
        this.handler = handler;
        this.command = command;
    }

    public void registerBundle(StackItBundle bundle) throws BundlerDisabledException{
        if (this.enabled){
            bundles.add(bundle);

            StackIt.LOGGER.success(String.format(BUNDLE_ENABLED_MESSAGE, bundle.getName()));
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
