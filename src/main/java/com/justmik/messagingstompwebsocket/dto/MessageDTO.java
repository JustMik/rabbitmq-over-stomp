package com.justmik.messagingstompwebsocket.dto;

public class MessageDTO {

	private String name;

	public MessageDTO() {
	}

	public MessageDTO(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "{'name': '" + name + "'}";
	}
}
