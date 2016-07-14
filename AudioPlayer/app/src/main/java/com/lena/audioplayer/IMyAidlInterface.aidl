// IMyAidlInterface.aidl
package com.lena.audioplayer;

import com.lena.audioplayer.Status;
// Declare any non-default types here with import statements

interface IMyAidlInterface {
     /** Request the process ID of this service, to do evil things with it. */
        Status getStatus();

        /** Demonstrates some basic types that you can use as parameters
         * and return values in AIDL.
         */
        void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
                double aDouble, String aString);
}
