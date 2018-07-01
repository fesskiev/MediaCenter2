package com.fesskiev.mediacenter.widgets.dialogs


import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.fesskiev.mediacenter.R
import kotlinx.android.synthetic.main.dialog_simple.*

class SimpleDialog : DialogFragment(), View.OnClickListener {

    interface OnPositiveListener {
        fun onClick()
    }

    interface OnNegativeListener {
        fun onClick()
    }

    companion object {

        private const val TITLE_TEXT = "extra.TITLE_TEXT"
        private const val MESSAGE_TEXT = "extra.MESSAGE_TEXT"
        private const val RES_ID = "extra.RES_ID"

        fun newInstance(title: String, message: String, resId: Int): SimpleDialog {
            val dialog = SimpleDialog()
            val args = Bundle()
            args.putString(TITLE_TEXT, title)
            args.putString(MESSAGE_TEXT, message)
            args.putInt(RES_ID, resId)
            dialog.arguments = args
            return dialog
        }
    }

    private var positiveListener: OnPositiveListener? = null
    private var negativeListener: OnNegativeListener? = null

    private var title: String? = null
    private var message: String? = null
    private var resId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomFragmentDialog)
        title = arguments?.getString(TITLE_TEXT)
        message = arguments?.getString(MESSAGE_TEXT)
        resId = arguments?.getInt(RES_ID, -1)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return activity?.layoutInflater?.inflate(R.layout.dialog_simple, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (title != null && !TextUtils.isEmpty(title)) {
            dialogTitle.text = title
        }
        if (message != null && !TextUtils.isEmpty(message)) {
            dialogMessage.text = message
        }
        if (resId != null && resId != -1) {
            dialogIcon.setImageResource(resId!!)
        }
        buttonPositive.setOnClickListener(this)
        buttonNegative.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.buttonPositive -> positiveListener?.onClick()
            R.id.buttonNegative -> negativeListener?.onClick()
        }
        dismiss()
    }

    fun setPositiveListener(l: OnPositiveListener) {
        this.positiveListener = l
    }

    fun setNegativeListener(l: OnNegativeListener) {
        this.negativeListener = l
    }
}