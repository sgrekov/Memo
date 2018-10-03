package com.factorymarket.memo.annotations;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseMemoised {

    private Map<String, Object> values = new HashMap<>();

    protected boolean isBooleanDifferent(String key, boolean value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new boolean[]{value});
            return true;
        }
        boolean memoizedValueUnwrapped = ((boolean[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((boolean[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isIntDifferent(String key, int value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new int[]{value});
            return true;
        }
        int memoizedValueUnwrapped = ((int[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((int[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isFloatDifferent(String key, float value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new float[]{value});
            return true;
        }
        float memoizedValueUnwrapped = ((float[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((float[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isDoubleDifferent(String key, double value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new double[]{value});
            return true;
        }
        double memoizedValueUnwrapped = ((double[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((double[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isLongDifferent(String key, long value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new long[]{value});
            return true;
        }
        long memoizedValueUnwrapped = ((long[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((long[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isShortDifferent(String key, short value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new short[]{value});
            return true;
        }
        short memoizedValueUnwrapped = ((short[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((short[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isCharDifferent(String key, char value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new char[]{value});
            return true;
        }
        char memoizedValueUnwrapped = ((char[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((char[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isByteDifferent(String key, byte value) {
        Object memoizedValue = values.get(key);
        if (memoizedValue == null) {
            values.put(key, new byte[]{value});
            return true;
        }
        byte memoizedValueUnwrapped = ((byte[]) memoizedValue)[0];
        if (value != memoizedValueUnwrapped) {
            ((byte[]) memoizedValue)[0] = value;
            values.put(key, memoizedValue);
            return true;
        }
        return false;
    }

    protected boolean isObjectDifferent(String key, Object value) {
        Object memoizedValue = values.get(key);
        if (value != memoizedValue) {
            if (value == null) {
                values.remove(key);
            } else {
                values.put(key, value);
            }
            return true;
        }
        return false;
    }

    protected boolean isStandartObjectDifferent(String key, Object value) {
        Object memoizedValue = values.get(key);
        if (value == null) {
            if (memoizedValue == null) {
                return false;
            } else {
                values.remove(key);
                return true;
            }
        }
        if (!value.equals(memoizedValue)) {
            values.put(key, value);
            return true;
        }
        return false;
    }

    protected void clearCache() {
        values.clear();
    }


}
