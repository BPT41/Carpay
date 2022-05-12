package com.example.carpay

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var mEditPrice : EditText
    private lateinit var mEditDown: EditText
    private lateinit var mRadioGroup:RadioGroup
    private lateinit var mEditRate:EditText
    private lateinit var mSpinner:Spinner
    private lateinit var mTextResult:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mEditPrice = findViewById(R.id.edText_Price)
        mEditDown = findViewById(R.id.edText_down)
        mEditRate = findViewById(R.id.ediText_Rate)
        //mRadioGroup = findViewById(R.id.radioButton2)
        //mRadioGroup = findViewById(R.id.radio_percent)

        mSpinner = findViewById(R.id.spinner)
        val items = MutableList(6){"${it+1} ปี"}
        items.add(0,"ระยะเวลาในการส่งงวด")
        val adapter = ArrayAdapter(baseContext,android.R.layout.simple_spinner_dropdown_item,items)
        mSpinner.adapter =adapter

        findViewById<Button>(R.id.button_calc).apply {
            setOnClickListener{
                //คำนวนณค่าผ่อนรถ
                calculate()
        }
            }
        mTextResult = findViewById(R.id.textView_result)
    }
    private fun calculate(){
     val price = mEditPrice.text.trim().toString().toInt()
     if(price <=0){
         //Toast.makeText()
         showMessage("ราคารถไม่ถูกต้อง")
     }
        var down = mEditDown.text.trim().toString().toDouble()
        down = price * (down/100)

        if(down <=0 || down>price){
            showMessage("เงินดาวน์ไม่ถูกต้อง")
            return
        }
        var rate = mEditRate.text.trim().toString().toDouble()
        rate /= 100 //ป้อน 24% => 24/100 =0.24
        rate /= 12  // 24% => 24/12 , 0.24 / 12 => ต่อเดือน

        val finance = price - down

        if(mSpinner.selectedItemPosition == 0){
            showMessage("กรุณาเลือกระยะเวลาการส่งงวด")
            return
        }
        val months  = mSpinner.selectedItemPosition * 12
        //ดอกเบี้ยทั้งหมด
        val interest = finance*months.toDouble()*rate

        //เงินที่ต้องจ่าย ช เงินต้น (finace) + ดอกเบี้ยรวม(interest)
        val total = finance + interest

        //จ่ายต่อเดือน
        val installment = total/months

        val format = NumberFormat.getNumberInstance()
        format.maximumFractionDigits = 2

        val result = format.format(installment)

        mTextResult.text = "ส่งงวดเดือนละ $result บาท"


        //mTextResult.setText(installment.toString())

    }
    private fun showMessage(msg : String){
        Toast.makeText(this,msg,Toast.LENGTH_LONG).show()

    //Snckbar.make(mTextResult,msg,Snackbar.LENGTH_LONG).show()
    }
}