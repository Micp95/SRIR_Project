package srir.frontend.views.compile



//import srir.shared.i18n.Translations
import io.udash.bootstrap.UdashBootstrap.ComponentId
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.{UdashForm, UdashInputGroup}
import io.udash.bootstrap.panel.UdashPanel
import io.udash.bootstrap.utils.UdashIcons.FontAwesome
import io.udash.css._
import io.udash.{ModelProperty, _}
//import io.udash.i18n._

class CompileView(
                    model: ModelProperty[CompileModel],
                    presenter: CompilePresenter
                 )
  extends FinalView with CssView {


  import scalatags.JsDom.all._


  // Standard Udash TextInput (we don't need Bootstrap Forms input wrapping)
  private val msgInput = TextInput.debounced(
    model.subProp(_.data),
    "TEST"
  )

  // Button from Udash Bootstrap wrapper
  private val submitButton = UdashButton(
    buttonStyle = ButtonStyle.Primary,
    block = true, componentId = ComponentId("send")
  )(span(FontAwesome.send), tpe := "submit")


  private val msgForm = div(
    UdashForm(
      _ => {
        presenter.sendFile()
        true // prevent default callback call
      }
    )(
      componentId = ComponentId("msg-from"),


      UdashInputGroup()(
        UdashInputGroup.input(msgInput.render),
        UdashInputGroup.buttons(submitButton.render)
      ).render

    ).render
  )



  override def getTemplate: Modifier = div(
    UdashPanel(componentId = ComponentId("chat-panel"))(
      UdashPanel.body(
        UdashForm.fileInput()("Select files")("files",
          acceptMultipleFiles = Property(false),
          selectedFiles = model.subSeq(_.selectedFile)
        )
      ),
      UdashPanel.footer(msgForm)
    ).render

  )
}
