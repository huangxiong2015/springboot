/*
 * Created: 2016年6月1日
 *
 * Shenzhen Yikuyi Co., Ltd. All rights reserved. 
 * Copyright (c) 2015-2016 
 * License, Version 1.0. Published by Yikuyi IT department.
 *
 * For the convenience of communicating and reusing of codes,
 * any java names, variables as well as comments should be written according to the regulations strictly.
 */
/**
 * 
 */
package com.ykyframework.oss;

/**
 * 阿里云OSS服务器端点的枚举类型。每个枚举值对应了相应的阿里云OSS服务器region。
 * @author liudian@yikuyi.com
 * @version 1.0.0
 */
public enum AliyunOSSEndpoint {
	OSS_CN_HANGZHOU {
		
		@Override
		public String getCNName() {
			return "华东 1 (杭州)";
		}
		
		@Override
		public String getRegionId() {
			return "cn-hangzhou";
		}
	},

	OSS_CN_SHANGHAI {
		
		@Override
		public String getCNName() {
			return "华东 2 (上海)";
		}
		
		@Override
		public String getRegionId() {
			return "cn-shanghai";
		}
	},

	OSS_CN_QINGDAO {
		
		@Override
		public String getCNName() {
			return "华北 1 (青岛)";
		}
		
		@Override
		public String getRegionId() {
			return "cn-qingdao";
		}
	},

	OSS_CN_BEIJING {
		
		@Override
		public String getCNName() {
			return "华北 2 (北京)";
		}
		
		@Override
		public String getRegionId() {
			return "cn-beijing";
		}
	},

	OSS_CN_SHENZHEN {
		
		@Override
		public String getCNName() {
			return "华南 1 (深圳)";
		}
		
		@Override
		public String getRegionId() {
			return "cn-shenzhen";
		}
	},

	OSS_CN_HONGKONG {
		
		@Override
		public String getCNName() {
			return "香港数据中心";
		}
		
		@Override
		public String getRegionId() {
			return "cn-hongkong";
		}
	},

	OSS_US_WEST_1 {
		
		@Override
		public String getCNName() {
			return "美国硅谷数据中心";
		}
		
		@Override
		public String getRegionId() {
			return "us-west-1";
		}
	},

	OSS_AP_SOUTHEAST_1 {
		
		@Override
		public String getCNName() {
			return "亚太（新加坡）数据中心";
		}
		
		@Override
		public String getRegionId() {
			return "ap-southeast-1";
		}
	};
	
	private static final String ALIYUN_DOMAIN = "aliyuncs.com";

	public abstract String getCNName();

	public String getENName() {
		return getRegionId();
	}
	
	public abstract String getRegionId();

	public String getUrl() {
		return "oss" + "-" + getRegionId() + "." + ALIYUN_DOMAIN;
	}
	
	public String getImgUrl() {
		return "img" + "-" + getRegionId() + "." + ALIYUN_DOMAIN;
	}
	
	public String getVpcUrl() {
		return "vpc100" + "-" + "oss" + "-" + getRegionId() + "." + ALIYUN_DOMAIN;
	}
	
	public String getVpcImgUrl() {
		return "vpc100" + "-" + "img" + "-" + getRegionId() + "." + ALIYUN_DOMAIN;
	}
	
	public String getInternalUrl() {
		return "oss" + "-" + getRegionId() + "-" + "internal" + "." + ALIYUN_DOMAIN;
	}
	
	public String getInternalImgUrl() {
		return "img" + "-" + getRegionId() + "-" + "internal" + "." + ALIYUN_DOMAIN;
	}
}