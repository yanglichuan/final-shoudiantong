package com.flashlightfree.utils;

import android.hardware.Camera;

public class PowerLED {
    boolean m_isOn;
    private Camera m_Camera;

    public boolean getIsOn() {
        return m_isOn;
    }

    public PowerLED() {
        m_isOn = false;
        m_Camera = Camera.open();
    }

    public void turnOn() {
        if (!m_isOn) {
            m_isOn = true;
            try {
                if (m_Camera == null) {
                    m_Camera = Camera.open();
                }
                Camera.Parameters mParameters;
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                m_Camera.setParameters(mParameters);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void turnOff() {
        if (m_isOn) {
            m_isOn = false;
            try {
                if (m_Camera == null) {
                    m_Camera = Camera.open();
                }
                Camera.Parameters mParameters;
                mParameters = m_Camera.getParameters();
                mParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                m_Camera.setParameters(mParameters);
                m_Camera.startPreview();
                m_Camera.autoFocus(new Camera.AutoFocusCallback() {
                    public void onAutoFocus(boolean success, Camera camera) {
                    }
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    public void Destroy() {
        if(m_Camera != null){
            m_Camera.stopPreview();
            m_Camera.release();
            m_Camera = null;
        }
    }

}
