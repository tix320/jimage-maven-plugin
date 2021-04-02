package com.github.tix320.plugins.jimage.jpakcage;

import java.util.List;

import com.github.tix320.plugins.jimage.common.Options;
import com.github.tix320.plugins.jimage.common.ValidationException;

/**
 * @author : Tigran Sargsyan
 * @since : 02.04.2021
 **/
public class LinuxOptions implements Options {

	@Override
	public List<String> toArgs() throws ValidationException {
		throw new UnsupportedOperationException("Not implemented yet for linux");
	}
}
