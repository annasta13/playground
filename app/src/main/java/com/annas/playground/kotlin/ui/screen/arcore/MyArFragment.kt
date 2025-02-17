//package com.annas.playground.kotlin.ui.screen.arcore
//
//import android.content.Context
//import com.google.ar.core.Config
//import com.google.ar.core.Session
//import com.google.ar.sceneform.ux.ArFragment
//
//
//// https://stackoverflow.com/questions/50402476/the-best-practice-to-integrate-arfragment-sceneform-with-existing-fragment-app
//class MyArFragment : ArFragment() {
//    interface OnCompleteListener {
//        fun onComplete()
//    }
//
//    private var mListener: OnCompleteListener? = null
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        kotlin.runCatching {
//            this.mListener = context as OnCompleteListener
//        }.onFailure {
//            println("check error on attach ${it.message}")
//        }
//    }
//
//    override fun getSessionConfiguration(session: Session): Config {
//        mListener?.onComplete()
//        val config = Config(session)
//        config.updateMode = Config.UpdateMode.LATEST_CAMERA_IMAGE
//        config.focusMode = Config.FocusMode.AUTO
//        session.configure(config)
//        return config
//    }
//
//}
