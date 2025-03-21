package com.userapp.util;

import org.modelmapper.ModelMapper;

public abstract class BaseModelMapper<M, D> {
	
	protected final ModelMapper modelMapper;
//	
	protected final Class<M> typeModel;
//	
	protected final Class<D> typeDto;
	
	public BaseModelMapper(ModelMapper modelMapper, Class<M> classModel, Class<D> classDto) {
		this.modelMapper = modelMapper;
		this.typeModel = classModel;
		this.typeDto = classDto;
	}
	 
	public D mapModelToDTO(M source) {
		return modelMapper.map(source, typeDto);
	}
	 
	public M mapDTOtoModel(D source) {
		return modelMapper.map(source, typeModel);
	}
	 
}
