package com.bhagavad.hifivedemo.server



public class ApiBuilderSingleton
{


    companion object {
        var mMethodBuilder: ApiInterface? = null;
        fun getInstance(): ApiInterface? {
            if(mMethodBuilder == null)
            {
                mMethodBuilder = ApiClientBuilder.getClient().create(ApiInterface::class.java);
            }else{
                //use else to reinitialize all headers again with updated value
                mMethodBuilder = null
                mMethodBuilder = ApiClientBuilder.getClient().create(ApiInterface::class.java);
            }
            return mMethodBuilder
        }
    }


    private fun ApiBuilderSingleton()
    {

    }
}