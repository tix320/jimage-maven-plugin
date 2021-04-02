package com.github.tix320.plugins.jimage.common;

import java.util.List;

public interface Options {

	List<String> toArgs() throws ValidationException;
}
