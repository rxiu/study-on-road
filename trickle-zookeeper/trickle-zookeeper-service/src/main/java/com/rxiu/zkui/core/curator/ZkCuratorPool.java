package com.rxiu.zkui.core.curator;

import com.rxiu.zkui.core.curator.pool.BoundedPool;
import com.rxiu.zkui.core.curator.validator.Validator;

/**
 * @author shenyuhang
 * @date 2019/5/28
 **/
public class ZkCuratorPool extends BoundedPool<ZkCurator> {

    public ZkCuratorPool(int size, Validator validator) {
        super(size, validator);
    }
}
