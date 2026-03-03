package com.njm.yaho.domain;

import com.njm.yaho.domain.mysql.admin.AnimeMSDTO;
import com.njm.yaho.domain.oracle.admin.AnimeOCDTO;

import lombok.Data;
@Data
public class MergeDTO {
	private AnimeMSDTO ms;
    private AnimeOCDTO oc;
}
