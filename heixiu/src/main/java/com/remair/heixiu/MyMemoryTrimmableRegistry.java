package com.remair.heixiu;

import com.facebook.common.memory.MemoryTrimType;
import com.facebook.common.memory.MemoryTrimmable;
import com.facebook.common.memory.MemoryTrimmableRegistry;

public class MyMemoryTrimmableRegistry implements MemoryTrimmableRegistry {

    @Override
    public void unregisterMemoryTrimmable(MemoryTrimmable trimmable) {

    }


    @Override
    public void registerMemoryTrimmable(MemoryTrimmable trimmable) {
        trimmable.trim(MemoryTrimType.OnSystemLowMemoryWhileAppInBackground);
    }
}