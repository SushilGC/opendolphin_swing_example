package org.opendolphin.swingexample

import groovy.transform.Canonical
import groovy.transform.Immutable
import org.opendolphin.binding.BindPojoOfAble
import org.opendolphin.binding.BindPojoToAble
import org.opendolphin.binding.BindToAble
import org.opendolphin.binding.Binder
import org.opendolphin.binding.UnbindFromAble
import org.opendolphin.binding.UnbindPojoFromAble
import org.opendolphin.binding.UnbindPojoOfAble
import org.opendolphin.core.Attribute
import org.opendolphin.core.PresentationModel
import org.opendolphin.core.Tag
import org.opendolphin.core.client.ClientAttribute
import org.opendolphin.core.client.ClientPresentationModel

import javax.swing.JComponent
import javax.swing.JTextField
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

class SwingBinder {
	static SwingBindOfAble bind(String sourcePropertyName) {
		new SwingBindOfAble(sourcePropertyName, Tag.VALUE)
	}

	static SwingBindOfAble bind(String sourcePropertyName, Tag tag) {
		new SwingBindOfAble(sourcePropertyName, tag)
	}

	static BindPojoOfAble bindInfo(String sourcePropertyName) {
		Binder.bindInfo(sourcePropertyName)
	}

	static SwingUnbindOfAble unbind(String sourcePropertyName) {
		new SwingUnbindOfAble(sourcePropertyName)
	}

	static UnbindPojoOfAble unbindInfo(String sourcePropertyName) {
		Binder.unbindInfo(sourcePropertyName)
	}
}

@Immutable
class SwingUnbindOfAble {
	String sourcePropertyName

	SwingUnbindFromAble of(JComponent source) {
		new SwingUnbindFromAble(source, sourcePropertyName)
	}

	UnbindFromAble of(PresentationModel source) {
		return Binder.unbind(sourcePropertyName).of(source)
	}

	UnbindClientFromAble of(ClientPresentationModel source) {
		new UnbindClientFromAble(source.findAttributeByPropertyName(sourcePropertyName))
	}

	UnbindPojoFromAble of(Object source) {
		return Binder.unbind(sourcePropertyName).of(source)
	}
}

class SwingUnbindFromAble {
	final JComponent source
	final String sourcePropertyName

	SwingUnbindFromAble(JComponent source, String sourcePropertyName) {
		this.source = source
		this.sourcePropertyName = sourcePropertyName
	}

	SwingUnbindOtherOfAble from(String targetPropertyName) {
		new SwingUnbindOtherOfAble(source, sourcePropertyName, targetPropertyName)
	}
}

class UnbindClientFromAble {
	final ClientAttribute attribute

	UnbindClientFromAble(ClientAttribute attribute) {
		this.attribute = attribute
	}

	UnbindClientOtherOfAble from(String targetPropertyName) {
		new UnbindClientOtherOfAble(attribute, targetPropertyName)
	}
}

class SwingUnbindOtherOfAble {
	final JComponent source
	final String sourcePropertyName
	final String targetPropertyName

	SwingUnbindOtherOfAble(JComponent source, String sourcePropertyName, String targetPropertyName) {
		this.source = source
		this.sourcePropertyName = sourcePropertyName
		this.targetPropertyName = targetPropertyName
	}

	void of(Object target) {
		def listener = new SwingBinderChangeListener(source, sourcePropertyName, target, targetPropertyName)
		// blindly remove the listener as Property does not expose a method to query existing listeners
		// javafx 2.2b17
		source."${sourcePropertyName}Property"().removeListener(listener)
	}
}

class UnbindClientOtherOfAble {
	final ClientAttribute attribute
	final String targetPropertyName

	UnbindClientOtherOfAble(ClientAttribute attribute, String targetPropertyName) {
		this.attribute = attribute
		this.targetPropertyName = targetPropertyName
	}

	void of(Object target) {
		def listener = new SwingBinderPropertyChangeListener(attribute, target, targetPropertyName)
		attribute.removePropertyChangeListener('value', listener)
	}
}


@Immutable
class SwingBindOfAble {
	String sourcePropertyName
	Tag    tag

	SwingBindToAble of(JComponent source) {
		new SwingBindToAble(source, sourcePropertyName)
	}

	BindToAble of(PresentationModel source) {
		return Binder.bind(sourcePropertyName, tag).of(source)
	}

	BindClientToAble    of(ClientPresentationModel source) {
		new BindClientToAble(source.findAttributeByPropertyNameAndTag(sourcePropertyName, tag))
	}

	BindPojoToAble of(Object source) {
		return Binder.bind(sourcePropertyName, tag).of(source)
	}
}

class SwingBindToAble {
	final JComponent source
	final String sourcePropertyName

	SwingBindToAble(JComponent source, String sourcePropertyName) {
		this.source = source
		this.sourcePropertyName = sourcePropertyName
	}

	SwingBindOtherOfAble to(String targetPropertyName) {
		new SwingBindOtherOfAble(source, sourcePropertyName, targetPropertyName)
	}
}

class BindClientToAble {
	final ClientAttribute attribute

	BindClientToAble(ClientAttribute attribute) {
		this.attribute = attribute
	}

	BindClientOtherOfAble to(String targetPropertyName) {
		new BindClientOtherOfAble(attribute, targetPropertyName)
	}
}

class SwingBindOtherOfAble {
	final JComponent source
	final String sourcePropertyName
	final String targetPropertyName

	SwingBindOtherOfAble(JComponent source, String sourcePropertyName, String targetPropertyName) {
		this.source = source
		this.sourcePropertyName = sourcePropertyName
		this.targetPropertyName = targetPropertyName
	}

	void of(Object target, Closure converter = null) {
		def listener = new SwingBinderChangeListener(source, sourcePropertyName, target, targetPropertyName, converter)
		source.addPropertyChangeListener(sourcePropertyName, listener)
		listener.update() // set the initial value after the binding and trigger the first notification
	}
}

class BindClientOtherOfAble {
	final ClientAttribute attribute
	final String targetPropertyName

	BindClientOtherOfAble(ClientAttribute attribute, String targetPropertyName) {
		this.attribute = attribute
		this.targetPropertyName = targetPropertyName
	}

	void of(Object target, Closure converter = null) {
		def listener = new SwingBinderPropertyChangeListener(attribute, target, targetPropertyName, converter)
		attribute.addPropertyChangeListener('value', listener)
		listener.update() // set the initial value after the binding and trigger the first notification
	}
}

@Canonical
class SwingBinderPropertyChangeListener implements PropertyChangeListener {
	Attribute attribute
	Object target
	String targetPropertyName
	Closure converter

	void update() {
		target[targetPropertyName] = convert(attribute.value)
	}

	void propertyChange(PropertyChangeEvent evt) {
		update()
	}

	Object convert(Object value) {
		converter != null ? converter(value) : value
	}
	// we have equals(o) and hashCode() from @Canonical
}

@Canonical
class SwingBinderChangeListener implements PropertyChangeListener {
	JComponent source
	String sourcePropertyName
	Object target
	String targetPropertyName
	Closure converter

	void update() {
		if (target instanceof PresentationModel) {
			target[targetPropertyName].value = convert(source[sourcePropertyName])
		} else {
			target[targetPropertyName] = convert(source[sourcePropertyName])
		}
	}

	@Override
	void propertyChange(final PropertyChangeEvent evt) {
		update()
	}

	Object convert(Object value) {
		converter != null ? converter(value) : value
	}

	// we have equals(o) and hashCode() from @Canonical
}
